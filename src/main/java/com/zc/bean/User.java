package com.zc.bean;

import lombok.*;

/**
 * Created by 7025 on 2017/10/30.
 */
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Getter @Setter private String name;
    @Getter @Setter private String phone;
}
