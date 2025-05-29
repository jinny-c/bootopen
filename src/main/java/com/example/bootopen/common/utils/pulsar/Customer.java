package com.example.bootopen.common.utils.pulsar;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author chaijd
 * @description TODO
 * @date 2024-07-03
 */
@Getter
@Setter
@ToString
public class Customer {
    private int id;
    private String name;
    private String address;
    private int count;

    public Customer(int id, String name, String address, int count) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.count = count;
    }

}
