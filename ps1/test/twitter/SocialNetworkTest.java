/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


import org.junit.Test;

public class SocialNetworkTest {

    /*
     * TODO: your testing strategies for these methods should go here.
     * See the ic03-testing exercise for examples of what a testing strategy comment looks like.
     * Make sure you have partitions.
     */
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    
//  guessFollowsGraph Function Tests
    
    @Test
    public void testGuessFollowsGraphEmpty() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(new ArrayList<>());
        
        assertTrue("expected empty graph", followsGraph.isEmpty());
    }
    
    @Test
    public void testGuessFollowsGraphNoMentions() {
        List<Tweet> tweets = List.of(
            new Tweet(1, "Ali", "Hello world!!!!", Instant.now()),
            new Tweet(2, "Ahmed", "This is a test tweet", Instant.now())
        );
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);
        assertTrue("expected empty graph when no mentions", followsGraph.isEmpty());
    }

    @Test
    public void testGuessFollowsGraphSingleMention() {
        List<Tweet> tweets = List.of(
            new Tweet(1, "Ali", "Hello @Ahmed", Instant.now())
        );
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);
        assertTrue("expected 'Ali' to follow 'Ahmed'", followsGraph.get("Ali").contains("Ahmed"));
    }

    @Test
    public void testGuessFollowsGraphMultipleMentions() {
        List<Tweet> tweets = List.of(
            new Tweet(1, "Ali", "Hello @Ahmed @Saad", Instant.now())
        );
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);
        assertTrue("expected 'Ali' to follow 'Ahmed'", followsGraph.get("Ali").contains("Ahmed"));
        assertTrue("expected 'Ali' to follow 'Saad'", followsGraph.get("Ali").contains("Saad"));
    }

    @Test
    public void testGuessFollowsGraphMultipleTweets() {
        List<Tweet> tweets = List.of(
            new Tweet(1, "Ali", "Hello @Ahmed", Instant.now()),
            new Tweet(2, "Ali", "Hi again @Saad", Instant.now())
        );
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);
        assertTrue("expected 'Ali' to follow 'Ahmed'", followsGraph.get("Ali").contains("Ahmed"));
        assertTrue("expected 'Ali' to follow 'Saad'", followsGraph.get("Ali").contains("Saad"));
    }
    
    
    
//    influencers Function Tests
    
    @Test
    public void testInfluencersEmpty() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        
        assertTrue("expected empty list", influencers.isEmpty());
    }

    @Test
    public void testInfluencersSingleUserNoFollowers() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("Ali", Set.of());
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertTrue("expected empty list when user has no followers", influencers.isEmpty());
    }

    @Test
    public void testInfluencersSingleUserWithFollowers() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("Ali", Set.of("Ahmed"));
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertEquals("expected 'Ahmed' to be the only influencer", List.of("Ahmed"), influencers);
    }

    @Test
    public void testInfluencersMultipleInfluencers() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("Ali", Set.of("Ahmed", "Saad"));
        followsGraph.put("Ahmed", Set.of("Saad"));
        followsGraph.put("Saad", Set.of());

        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertEquals("expected 'Saad' to be the top influencer", "Saad", influencers.get(0));
        assertEquals("expected 'Ahmed' to be the second influencer", "Ahmed", influencers.get(1));
    }


}
