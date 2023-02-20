package com.example.Repo;

import com.example.Model.User;
import com.example.Model.Item;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends CrudRepository<Item, Long> {

    public List<Item> findByUser(User user);

    @Query("SELECT i FROM Item i WHERE CONCAT(i.itemName, i.itemDesc, i.category) LIKE %?1%")
    public List<Item> search(String keyword);

}
