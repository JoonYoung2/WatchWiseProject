package com.watch.project.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.ibatis.binding.BindingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.watch.project.dto.MovieReviewDTO;
import com.watch.project.repository.ReviewRepository;

@Service
public class ReviewService {
	@Autowired
	private ReviewRepository repo;
	@Autowired
	private HttpSession session;

	public String insertOrUpdateScore(String movieId, float rating) {
		String msg = "";
		if(rating == 0.5){
			msg = "최악이에요";
		}else if(rating == 1.0){
			msg = "싫어요";
		}else if(rating == 1.5){
			msg = "재미없어요";
		}else if(rating == 2.0){
			msg = "별로예요";
		}else if(rating == 2.5){
			msg = "부족해요";
		}else if(rating == 3.0){
			msg = "보통이에요";
		}else if(rating == 3.5){
			msg = "볼만해요";
		}else if(rating == 4.0){
			msg = "재미있어요";
		}else if(rating == 4.5){
			msg = "훌륭해요";
		}else if(rating == 5.0){
			msg = "최고예요";
		}
		String pk = movieId+(String)session.getAttribute("userEmail");
		MovieReviewDTO result = repo.getInfoByPk(pk);
		if(result != null) {//이미 평가를 했던 사람
			result.setReviewScore(rating);
			int updateResult = repo.updateScore(result);
			if(updateResult != 1) {
				msg = "오류가 발생했습니다. 다시 시도해주세요.";
			}
		}else { //처음으로 평가하는 사람
//			Map<String, Object> map = new HashMap<>();
//			map.put("movieId", movieId);
//			map.put("rating", rating);
			MovieReviewDTO rookie = new MovieReviewDTO();
			rookie.setId(pk);
			rookie.setMovieId(movieId);
			rookie.setUserEmail((String)session.getAttribute("userEmail"));
			rookie.setReviewScore(rating);
			rookie.setReviewComment("nan");
//			rookie.setReviewCommentDate("nan");
//			if(rating == 0.5){
//				msg = "최악이에요";
//			}else if(rating == 1.0){
//				msg = "싫어요";
//			}else if(rating == 1.5){
//				msg = "재미없어요";
//			}else if(rating == 2.0){
//				msg = "별로예요";
//			}else if(rating == 2.5){
//				msg = "부족해요";
//			}else if(rating == 3.0){
//				msg = "보통이에요";
//			}else if(rating == 3.5){
//				msg = "볼만해요";
//			}else if(rating == 4.0){
//				msg = "재미있어요";
//			}else if(rating == 4.5){
//				msg = "훌륭해요";
//			}else if(rating == 5.0){
//				msg = "최고예요";
//			}
			int insertResult = repo.insertScore(rookie);
			if(insertResult != 1) {
				msg = "오류가 발생했습니다. 다시 시도해주세요.";
			}
		}
		return msg;
	}

	public String insertComment(MovieReviewDTO dto) {//movieId, reviewComment
		String msg = "저장되었습니다.";
		String pk = dto.getMovieId()+(String)session.getAttribute("userEmail");
		float existance = checkScore(pk);
		
		System.out.println(existance);
		System.out.println("*******************************111***********************************");
		
		Date currentDate = new Date(); //Fri Oct 27 21:35:40 KST 2023
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");// Date를 ISO 8601 형식의 문자열로 변환
		String dateStr = dateFormat.format(currentDate);//2023-10-27T21:35:40Z
		
		System.out.println(dateStr);
		System.out.println("*******************************222***********************************");

		dto.setId(pk);
		dto.setUserEmail((String)session.getAttribute("userEmail"));
		dto.setReviewScore(existance);
		dto.setReviewCommentDate(dateStr);
		
		if(existance != 0) {//평점을 먼저한 사람 => 이미 데이터 행이 있음. update
			int updateResult = repo.updateForComment(dto);
			if(updateResult != 1) {
				msg = "오류가 발생했습니다. 다시 시도해주세요.";
			}
		} else {//코멘트를 먼저하는 사람 => 데이터 행 없음. insert
			int storageResult = repo.insertForComment(dto);
			if(storageResult != 1) {
				msg = "오류가 발생했습니다. 다시 시도해주세요.";
			}
		}
		return msg;
	}

	private float checkScore(String id) {
//		float reviewScore = 100;
//		try {
		float reviewScore = repo.getStoredScore(id);			
//		}catch(Exception e){
//			System.out.println("에러 발생");
//		}
//		if(reviewScore == 0) {
//			return 0;
//		}else {
			return reviewScore;
//		}
	}

	public List<MovieReviewDTO> getEveryCommentForThisMovie(String movieId) {
		List<MovieReviewDTO> comments = repo.selectComments(movieId);
		return comments;
	}
}
