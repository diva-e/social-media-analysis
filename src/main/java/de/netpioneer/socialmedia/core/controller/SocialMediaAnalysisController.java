package de.netpioneer.socialmedia.core.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.netpioneer.socialmedia.core.domain.Sentence;
import de.netpioneer.socialmedia.sentimentanalysis.domain.Sentiment;
import de.netpioneer.socialmedia.sentimentanalysis.service.SentimentAnalysisService;
import de.netpioneer.socialmedia.twitter.service.TweetToSentenceConversionService;
import de.netpioneer.socialmedia.twitter.service.TwitterService;
import de.netpioneer.socialmedia.wordprocessing.service.WordFrequencyService;

@RequestMapping(value = "/social-media-analysis/v1.0/", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class SocialMediaAnalysisController {

	@Autowired
	private SentimentAnalysisService sentimentAnalysisService;
	
	@Autowired
	private WordFrequencyService wordFrequencyService;
	
	@Autowired
	private TwitterService twitterService;
	
	@Autowired
	private TweetToSentenceConversionService tweetToSentenceConversionService;
	
	@RequestMapping(value = "sentiments", method = RequestMethod.GET)
	public Map<Sentiment, Integer> getSentimentsForTweetsByKeyword(@RequestParam String keyword) {
		List<Sentence> sentences = findSentencesByKeyword(keyword, "en");
		return sentimentAnalysisService.getSentimentFrequenciesForSentences(sentences);
	}
	
	@RequestMapping(value = "wordfrequencies", method = RequestMethod.GET)
	public Map<String, Integer> getWordFrequenciesForTweetsByKeyword(@RequestParam String keyword) {
		List<Sentence> sentences = findSentencesByKeyword(keyword, "de");
		return wordFrequencyService.calculateWordFrequencies(sentences);
	}

	private List<Sentence> findSentencesByKeyword(String keyword, String language) {
		List<Tweet> tweets = twitterService.findTweetsByKeyword(keyword, language);
		List<Sentence> sentences = tweetToSentenceConversionService.convertTweetsToSentences(tweets);
		return sentences;
	}
	
}
