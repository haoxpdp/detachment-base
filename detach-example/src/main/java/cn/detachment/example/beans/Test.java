package cn.detachment.example.beans;

import lombok.Data;

/**
 * @author haoxp
 * @date 20/12/15
 */
@Data
public class Test {

    private String name;

    public static void main(String[] args) {
        int[] nums = {1, 3, 4, 5, 4, 23, 4, 61, 4,  3, 4};
        System.out.println(majorityElement(nums));
    }

    public static int majorityElement(int[] nums) {
        // 投票算法
        int temp = nums[0];
        int count = 1;
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] == temp) {
                count++;
            } else {
                count--;
            }
            if (count == 0) {
                temp = nums[i];
                count = 1;
            }
        }

        // 验证是否满足要求
        int t = nums.length / 2 + 1;
        count = 0;
        for (int num : nums) {
            if (num == temp) count++;
            if (count == t) return temp;
        }
        return -1;
    }
}
