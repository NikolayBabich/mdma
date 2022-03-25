package ru.filit.mdma.dm.repository;

import java.io.InputStream;
import java.util.List;

public interface ReadableRepository<T> {

  List<T> readAll(InputStream is);

}
