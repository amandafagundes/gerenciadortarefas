package com.strider.desafio.dto;

import com.strider.desafio.model.Task;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

@Data
public class TaskDTO {
    private Integer id;
    private String title;
    private String image;
    private String status;
    private Double latitude;
    private Double longitude;


    public static Task toEntity(TaskDTO cond) {
        Task task = new Task();
        BeanUtils.copyProperties(cond, task);
        return task;
    }

    public static TaskDTO fromEntity(Task task) {
        TaskDTO taskDTO = new TaskDTO();
        BeanUtils.copyProperties(task, taskDTO);
        return taskDTO;
    }

    public static ArrayList<TaskDTO> fromList(List<Task> ls) {
        ArrayList<TaskDTO> lst = new ArrayList<>();
        ls.forEach(taskDTO -> {
            lst.add(fromEntity(taskDTO));
        });
        return lst;
    }

}
