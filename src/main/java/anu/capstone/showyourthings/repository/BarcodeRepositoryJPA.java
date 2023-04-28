package anu.capstone.showyourthings.repository;

import anu.capstone.showyourthings.entity.BarcodeInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BarcodeRepositoryJPA extends JpaRepository<BarcodeInfo, String> {

}
