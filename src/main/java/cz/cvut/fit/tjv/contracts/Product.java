package cz.cvut.fit.tjv.contracts;

import cz.cvut.fit.tjv.domain.Order;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Collection;

public class Product {
    private Integer id;
    private String name;
    private String description;
    private Double price;
    private Integer avaliableAmount;

}
