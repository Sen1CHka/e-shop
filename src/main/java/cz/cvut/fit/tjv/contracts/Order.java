package cz.cvut.fit.tjv.contracts;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.cvut.fit.tjv.domain.Product;
import cz.cvut.fit.tjv.domain.State;
import cz.cvut.fit.tjv.domain.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Collection;

public class Order {

    private Integer id;

    private Integer UserId;

    private String UserName;

    private Collection<Product> products;

    private LocalDateTime date;

    private String state;
}
