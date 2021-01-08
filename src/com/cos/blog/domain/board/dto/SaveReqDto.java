package com.cos.blog.domain.board.dto;

import lombok.Data;

@Data
public class SaveReqDto {
	private String title;
	private String content;
	private int userId;
}
