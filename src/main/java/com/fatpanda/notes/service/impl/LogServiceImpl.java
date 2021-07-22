package com.fatpanda.notes.service.impl;

import com.fatpanda.notes.pojo.entity.Log;
import com.fatpanda.notes.repository.LogRepository;
import com.fatpanda.notes.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("logService")
public class LogServiceImpl implements LogService {


	@Resource
	private LogRepository logRepository;

    @Override
    public int saveLog(Log log) {
        logRepository.save(log);
        return 1;
    }

}
