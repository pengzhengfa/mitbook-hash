package com.mitbook.construct;

import java.util.Arrays;
import java.util.Comparator;

/**
 * 根绝key排序的map
 */
public class SortArrayMap {

    /**
     * 核心数组
     */
    private Node[] buckets;

    private static final int DEFAULT_SIZE=10;

    /**
     * 数组大小
     */
    private int size =0;

    public SortArrayMap(){
        buckets = new Node[DEFAULT_SIZE];
    }

    /**
     * 写入数据
     * @param key
     * @param value
     */
    public void add(Long key,String value){
        checkSize(size + 1);
        Node node = new Node(key,value);
        buckets[size++] = node;
    }

    /**
     * 校验是否需要扩容
     * @param size
     */
    private void checkSize(int size){
        if(size>=buckets.length){
            //3/2
            int oldLen = buckets.length;
            int newLen = oldLen + (oldLen >> 1);
            buckets = Arrays.copyOf(buckets,newLen);
        }
    }

    /**
     * 顺时针取出数据
     * @param key
     * @return
     */
    public String fristNodeValue(long key) {
        if (size == 0) {
            return null;
        }
        for (Node bucket : buckets) {
            if (bucket == null) {
                break;
            }
            if (bucket.key >= key) {
                return bucket.value;
            }

        }
        return buckets[0].value;
    }

    /**
     * 排序
     */
    public void sort(){
        Arrays.sort(buckets, 0, size, new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                if(o1.key >o2.key){
                    return 1;
                }else {
                    return -1;
                }
            }
        });
    }

    public int size(){
        return  size;
    }

    private class Node{
        public Long key;
        public String value;

        public Node(Long key, String value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "key=" + key +
                    ", value='" + value + '\'' +
                    '}';
        }
    }
}
