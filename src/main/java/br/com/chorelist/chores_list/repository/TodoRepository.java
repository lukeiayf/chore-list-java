package br.com.chorelist.chores_list.repository;

import br.com.chorelist.chores_list.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long> {
}
