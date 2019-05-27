package com.strider.desafio.controller;

import com.strider.desafio.dto.TaskDTO;
import com.strider.desafio.model.Task;
import com.strider.desafio.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.json.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@CrossOrigin//para aceitar requisições de diferentes domínios
@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskRepository repository;

    @Autowired
    private EntityManager manager;

    @GetMapping(path = "/info/{id}")
    public ResponseEntity<TaskDTO> getTask(@PathVariable Integer id) {
        Task task = repository.getOne(id);
        if (task != null) {
            System.out.println("achou");
            TaskDTO dto = TaskDTO.fromEntity(task);
            return ResponseEntity.ok(dto);
        } else {
            System.out.println("nao achou");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping(path = "/list")
    public ResponseEntity<List<TaskDTO>> listTasks(@RequestBody Optional<String> status) {
        List<Task> tasks;
        if (!status.isPresent()) {
            tasks = repository.findAll();
        } else {
            tasks = repository.findByStatus(new JSONObject(status.get()).get("status").toString());
        }
        return ResponseEntity.ok(TaskDTO.fromList(tasks));
    }

    @PostMapping(path = "/create")
    public ResponseEntity<TaskDTO> addTask(@RequestBody TaskDTO dto) {
        Task task = TaskDTO.toEntity(dto);
        repository.save(task);
        return ResponseEntity.ok(TaskDTO.fromEntity(task));
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Integer id) {
        repository.deleteById(id);
        return ResponseEntity.ok("");
    }

    @PutMapping(path = "/update")
    public ResponseEntity<TaskDTO> updateTask(@RequestBody TaskDTO dto) {
        Task task = TaskDTO.toEntity(dto);
        repository.save(task);
        return ResponseEntity.ok(TaskDTO.fromEntity(task));
    }

    @Transactional
    @PostMapping(path = "/upload/{id}")
    public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile file, @PathVariable("id") Integer id) {
        try {

            String path = System.getProperty("user.home") + "/strider/uploads/";
            String fileName = path + "task" + id + "." + file.getOriginalFilename().split("\\.", 2)[1];
            File image = new File(fileName);
            BufferedOutputStream stream = new BufferedOutputStream(
                    new FileOutputStream(image));
            stream.write(file.getBytes());
            stream.close();

            try {
                Files.copy(file.getInputStream(), Paths.get(fileName));
            } catch (NoSuchFileException e) {
                new File(path).mkdirs();
                Files.copy(file.getInputStream(), Paths.get(fileName));
            } catch (FileAlreadyExistsException e) {
                new File(fileName).delete();
                Files.copy(file.getInputStream(), Paths.get(fileName));
            }
            Task task = repository.getOne(id);
            task.setImage(fileName);
            repository.save(task);
            return ResponseEntity.status(HttpStatus.OK).body("");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("");
        }
    }

    @RequestMapping(value = "/image", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public FileSystemResource getImage(@RequestParam(name = "id") String id) {
        Task task = repository.getOne(Integer.parseInt(id));
        if(task.getImage() == null) return null;
        File file = new File(task.getImage());
        return file.exists() ? new FileSystemResource(file) : null;

    }

}
