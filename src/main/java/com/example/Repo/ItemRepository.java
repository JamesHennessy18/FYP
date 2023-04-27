package com.example.Repo;

import com.example.Model.User;
import com.example.Model.Item;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends CrudRepository<Item, Long> {

    public List<Item> findByUser(User user);

    @Query("SELECT i FROM Item i WHERE i.itemId = ?1")
    Item findByItemId(Long itemId);

    @Query("SELECT i FROM Item i WHERE CONCAT(i.itemName, i.itemDesc, i.category) LIKE %?1%")
    public List<Item> search(String keyword);

    @Query(value = "select * from ms_item where is_available=true",nativeQuery = true)
    List<Item> findAll();

    @Query(value = "SELECT * FROM ms_Item WHERE is_Available = 0 AND item_Name LIKE %:itemName% ORDER BY item_Id DESC LIMIT 5", nativeQuery = true)
    List<Item> findLastFiveSoldItemsByItemName(@Param("itemName") String itemName);

    @Query(value = "SELECT * FROM ms_Item WHERE is_Available = 0 ORDER BY item_Id DESC LIMIT 5", nativeQuery = true)
    List<Item> findLastFiveSoldItems();


}
