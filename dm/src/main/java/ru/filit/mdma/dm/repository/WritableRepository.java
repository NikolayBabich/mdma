package ru.filit.mdma.dm.repository;

import java.util.List;

public interface WritableRepository<T> {

  void writeAll(List<T> entities);

}
