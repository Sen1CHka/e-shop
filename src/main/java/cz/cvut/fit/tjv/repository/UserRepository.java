package cz.cvut.fit.tjv.repository;

import cz.cvut.fit.tjv.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends CrudRepository<User, String> {
}
