package com.ajaxjs.load_balance;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 一致性哈希对象
 */
public class ConsistentHash<T> {
    private TreeMap<Integer, T> ring = new TreeMap<>();


    public ConsistentHash(T node, int replicas) {
        addNode(node, replicas);
    }

    public void addNode(T node, int replicas) {
        for (int i = 0; i < replicas; i++) {
            int hash = hash(node.toString() + i);
            ring.put(hash, node);
        }
    }

    public void removeNode(T node, int replicas) {
        for (int i = 0; i < replicas; i++) {
            int hash = hash(node.toString() + i);
            ring.remove(hash);
        }
    }

    public static int hash(String key) {
        int hash = 0;
        for (int i = 0; i < key.length(); i++) {
            hash = 31 * hash + key.charAt(i);
        }
        return hash;
    }

    public T getNode(Object key) {
        if (ring.isEmpty())
            return null;

        int hash = hash(key.toString());
        Map.Entry<Integer, T> entry = ring.ceilingEntry(hash);
        if (entry == null)
            entry = ring.firstEntry();

        return entry.getValue();
    }

    public List<T> getNUniqueBinsFor(String serviceName, int size) {
        return null;
    }
}
