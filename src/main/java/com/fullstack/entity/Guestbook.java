package com.fullstack.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class Guestbook extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long ago;

	@Column(length = 100, nullable = false)
	private String title;

	@Column(length = 100, nullable = false)
	private String content;

	@Column(length = 100, nullable = false)
	private String writer;

	/*
	 * 나중에 방명록을 작성 후 내용 및 제목을 수정할 경우 이 내용을 반영토록 하기위해 Setter 정의합니다.
	 */
	public void changeTitle(String title) {
		this.title = title;
	}

	public void changeContent(String content) {
		this.content = content;
	}
}