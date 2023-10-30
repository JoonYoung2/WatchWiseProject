package com.watch.api.scheduled.movie;

import java.io.UnsupportedEncodingException;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.watch.api.repository.MovieInfoApiRepository;
import com.watch.api.service.MovieInfoApiService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class TopUpdateScheduled {
	private final MovieDailyUpdateScheduled dailyUpdate;
	private final MovieWeeklyUpdateScheduled weeklyUpdate;
	@Scheduled(cron = "35 43 9 * * *")
	public void allUpdateProcess() throws UnsupportedEncodingException {
		dailyUpdate.allFindAndInsert();
		dailyUpdate.DailyMovieInsert();
		weeklyUpdate.WeeklyMovieInsert0();
		weeklyUpdate.WeeklyMovieInsert1();
		weeklyUpdate.WeeklyMovieInsert2();
	}
}
