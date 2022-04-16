package ru.filit.mdma.crm.repository;

import java.util.List;
import ru.filit.mdma.crm.model.User;

public interface UserRepository {

  List<User> readAllUsers();

}
