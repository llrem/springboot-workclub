package com.yu.common.hashing;

public class Hash {
    int[] table;
    int length;
    public Hash(int length){
        table = new int[length];
        this.length = length;
    }

    public void put(int key){
        int i = key % length;
        for (int j = 0; j < length; j++) {
            if (table[i] != 0 && table[i] != key) {
                i = (i + 1) % length;
            } else {
                break;
            }
        }
        table[i] = key;
    }

    public int getIndexForKey(int key){
        int i = key % length;
        for(int j=0;j<length;j++){
            if(table[i]!=0 && table[i]!=key){
                i = (i+1) % length;
            }else{
                break;
            }
        }
        return i;
    }

    public boolean isContainsKey(int key){
        int i = key % length;
        for(int j=0;j<length;j++){
            if(table[i]==0){
                return false;
            }else if(table[i]!=0&&table[i]!=key){
                i = (i+1) % length;
            }
            else{
                return true;
            }
        }
        return false;
    }
}
