package com.example.testqq.vules;

import com.example.testqq.R;

import java.util.HashMap;

/**
 * Created by 宋宝春 on 2017/4/26.
 */

public class Image {
    public static HashMap<String, Integer> hashMap = new HashMap<>();
    public static int[] s = {R.drawable.sp1, R.drawable.s2,
            R.drawable.s3, R.drawable.s4, R.drawable.s5, R.drawable.s6, R.drawable.s7, R.drawable.s2,
            R.drawable.s3, R.drawable.s4, R.drawable.s5, R.drawable.s6, R.drawable.s7};
    public static String[] str = {"[惊讶]", "[委屈]", "[色]", "[脸红]", "[酷]", "[衰]", "[困]", "[委屈]", "[色]", "[脸红]", "[酷]", "[衰]", "[困]"};

    public static void setImg() {
        for (int j = 0; j < str.length; j++) {
            hashMap.put(str[j], s[j]);
        }
    }
    public static int getImae(String k){
        if (hashMap.size()==0){
            setImg();
        }
        return hashMap.get(k);
    }
}
