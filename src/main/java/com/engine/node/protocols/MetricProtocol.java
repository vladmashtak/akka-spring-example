package com.engine.node.protocols;

import akka.cluster.metrics.NodeMetrics;
import akka.cluster.metrics.StandardMetrics;
import akka.cluster.metrics.StandardMetrics.Cpu;
import akka.cluster.metrics.StandardMetrics.HeapMemory;

import java.io.Serializable;

public class MetricProtocol implements Serializable {
    private Cpu cpu;
    private HeapMemory heap;
    private String nodeAddress;

    public MetricProtocol(NodeMetrics nodeMetrics) {
        this.cpu = StandardMetrics.extractCpu(nodeMetrics);
        this.heap = StandardMetrics.extractHeapMemory(nodeMetrics);
        this.nodeAddress = nodeMetrics.address().toString();
    }

    public Cpu getCpu() {
        return cpu;
    }

    public HeapMemory getHeap() {
        return heap;
    }

    public String getNodeAddress() {
        return nodeAddress;
    }

    @Override
    public String toString() {
        StringBuilder metrics = new StringBuilder()
                .append(nodeAddress)
                .append(" { ");

        if (heap != null) {
            double heapUsed = ((double) heap.used()) / 1024 / 1024;
            metrics.append("Used heap: ")
                    .append(heapUsed)
                    .append("MB | ");
        }

        if (cpu != null && cpu.systemLoadAverage().isDefined()) {
            double loadAverage = (double) cpu.systemLoadAverage().get();
            int proc = cpu.processors();

            metrics.append("Load: ")
                    .append(loadAverage)
                    .append("%, ")
                    .append(proc)
                    .append(" processors");
        }

        return metrics
                .append(" }")
                .toString();
    }
}