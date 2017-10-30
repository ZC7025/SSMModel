package com.zc.dao;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProcessDAO {
    void save(Process process);
    List<Process> listAll();
}
