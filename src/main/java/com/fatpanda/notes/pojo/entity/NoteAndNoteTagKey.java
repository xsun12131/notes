package com.fatpanda.notes.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class NoteAndNoteTagKey implements Serializable{

    private String noteId;

    private String noteTagId;

}
