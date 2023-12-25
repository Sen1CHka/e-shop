package cz.cvut.fit.tjv.contracts;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.cvut.fit.tjv.domain.Order;
import cz.cvut.fit.tjv.domain.Product;
import cz.cvut.fit.tjv.domain.State;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Collection;

public class User {

    private String username;
    private String realName;
    private String email;
}
