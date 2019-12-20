package com.niil.nogor.krishi.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.niil.nogor.krishi.entity.AddressType;
import com.niil.nogor.krishi.entity.Orders;
import com.niil.nogor.krishi.entity.Product;
import com.niil.nogor.krishi.entity.User;
import com.niil.nogor.krishi.entity.UserAddressPreference;

@Repository
public interface UserAddressPreferenceRepo extends JpaRepository<UserAddressPreference, Long>{

	List<UserAddressPreference> findAllByUser(User user);
	List<UserAddressPreference> findAllByUserAndAddressType(User user, AddressType addressType);

}
