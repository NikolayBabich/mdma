package ru.filit.mdma.crm.repository;

import java.util.List;
import ru.filit.mdma.crm.model.UserExtended;

public interface UserRepository {

  List<UserExtended> getAllUsers();

}
