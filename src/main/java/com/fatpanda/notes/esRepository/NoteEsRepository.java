package com.fatpanda.notes.esRepository;

import com.fatpanda.notes.pojo.esEntity.EsNote;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface NoteEsRepository extends ElasticsearchRepository<EsNote, String> {

    /**
     * 批量删除User及更新删除标识
     *
     * @param idList idList
     */
    @Transactional(rollbackOn = Exception.class)
    void deleteByIdIn(List<String> idList);

}
