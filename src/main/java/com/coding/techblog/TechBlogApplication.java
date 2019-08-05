package com.coding.techblog;

import com.coding.techblog.modal.Vo.CommentVo;
import com.coding.techblog.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.logging.Logger;

@SpringBootApplication
public class TechBlogApplication{

	Logger logger = Logger.getLogger(getClass().getName());


	@Autowired
	private ICommentService commentService;

	public static void main(String[] args) {
		SpringApplication.run(TechBlogApplication.class, args);
	}

}
