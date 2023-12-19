package cz.cvut.fit.tjv.repositary;

import cz.cvut.fit.tjv.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends CrudRepository<User, String> {
}
