package com.example.android.writeitsayithearit.test.data

import android.app.Application
import android.content.Context
import com.example.android.writeitsayithearit.model.author.Author
import com.example.android.writeitsayithearit.model.comment.Comment
import com.example.android.writeitsayithearit.model.cue.Cue
import com.example.android.writeitsayithearit.model.story.Story
import org.json.JSONArray
import org.json.JSONObject
import timber.log.Timber
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.util.*
import javax.inject.Inject

/**
 * Object that loads list of models from JSON asset files into memory.
 *
 * It exposes this data to be used in seeding the database and testing.
 *
 * This pattern allows data to be created in a more readable fashion.
 *
 * TODO: It does have the drawback of creating a I/O operation every time
 * the database is seeded (which is before every test). This could lead to
 * slower tests
 */
class DatabaseSeed @Inject constructor(application: Application) {

    /**
     * Calendar is used to create creation dates that are relative to today's
     * date. This allows for predetermined values to be returned when data is
     * filtered by 'Hot' (which filters out everything older than 24 hours)
     *
     * It must be initialized before the data is read.
     */
    private val calendar: Calendar = Calendar.getInstance()

    val SEED_AUTHORS:List<Author>
    val SEED_CUES:List<Cue>
    val SEED_STORIES:List<Story>
    val SEED_COMMENTS:List<Comment>

    init {
        SEED_CUES = loadCues("fake_cues.json", application)
        SEED_AUTHORS = loadAuthors("fake_authors.json", application)
        SEED_STORIES  = loadStories("fake_stories.json", application)
        SEED_COMMENTS = loadComments("fake_comments.json", application)
    }

    private fun loadAuthors(fileName: String, context: Context): List<Author> {
        val content = readJSONFromAssets(fileName, context)
        val authors :MutableList<Author> = mutableListOf()
        try {
            val json = JSONObject(content)
            val jsonAuthors: JSONArray = json.getJSONArray("authors")
            for(i in 0 until jsonAuthors.length()) {
                val jsonAuthor = jsonAuthors.getJSONObject(i)

                val name = jsonAuthor.getString("name")
                val author = Author(name)
                authors.add(author)
            }
        }catch(e: Exception) {
            Timber.e("Error initializing JSON Object from authors.")
            e.printStackTrace()
        }
        return authors
    }

    private fun loadCues(fileName: String, context: Context): List<Cue> {
        val content = readJSONFromAssets(fileName, context)
        val cues :MutableList<Cue> = mutableListOf()
        try {
            val json = JSONObject(content)
            val jsonCues: JSONArray = json.getJSONArray("cues")
            for(i in 0 until jsonCues.length()) {
                val jsonCue = jsonCues.getJSONObject(i)

                val text = jsonCue.getString("text")
                val author = jsonCue.getString("author")
                val rating = jsonCue.getInt("rating")
                val cue = Cue(text, author, creationDate(i), rating, 0)
                cues.add(cue)
            }
        }catch(e: Exception) {
            Timber.e("Error initializing JSON Object from cues.")
            e.printStackTrace()
        }
        return cues
    }

    fun loadStories(fileName: String, context: Context): List<Story> {
        val content = readJSONFromAssets(fileName, context)
        val stories :MutableList<Story> = mutableListOf()
        try {
            val json = JSONObject(content)
            val jsonStories: JSONArray = json.getJSONArray("stories")
            for(i in 0 until jsonStories.length()) {
                val jsonStory = jsonStories.getJSONObject(i)

                val text = jsonStory.getString("text")
                val author = jsonStory.getString("author")
                val cueId = jsonStory.getInt("cue_id")
                val rating = jsonStory.getInt("rating")
                val story = Story(text, author, cueId, creationDate(i), rating, 0)
                stories.add(story)
            }
        }catch(e: Exception) {
            Timber.e("Error initializing JSON Object from stories.")
            e.printStackTrace()
        }
        return stories
    }

    private fun loadComments(fileName: String, context: Context): List<Comment> {
        val content = readJSONFromAssets(fileName, context)
        val comments :MutableList<Comment> = mutableListOf()
        try {
            val json = JSONObject(content.toString())
            val jsonComments: JSONArray = json.getJSONArray("comments")
            for(i in 0 until jsonComments.length()) {
                val jsonComment = jsonComments.getJSONObject(i)
                val id = jsonComment.getInt("id")
                val storyId = jsonComment.getInt("story_id")
                val parentId = jsonComment.getInt("parent_id")
                val depth = jsonComment.getInt("depth")
                val text = jsonComment.getString("text")
                val author = jsonComment.getString("author")
                val rating = jsonComment.getInt("rating")
                val creationDate = creationDate(i)

                val comment = Comment(
                    id = id,
                    storyId = storyId,
                    parentId = parentId,
                    depth = depth,
                    text = text,
                    author = author,
                    rating = rating,
                    creationDate = creationDate
                )
                comments.add(comment)
            }
        }catch(e: Exception) {
            Timber.e("Error initializing JSON Object from authors.")
            e.printStackTrace()
        }
        return comments
    }

    private fun readJSONFromAssets(fileName: String, context: Context): String {
        var inputStream: InputStream? = null
        try {
            inputStream = context.assets.open(fileName)
        } catch (e: IOException) {
            Timber.e("Error reading JSON asset from $fileName")
            e.printStackTrace()
        }
        val bufferedBufferedReader = BufferedReader(
            InputStreamReader(inputStream)
        )

        val content = StringBuilder()
        try {
            var line = bufferedBufferedReader.readLine()
            while (line != null) {
                content.append(line)
                line = bufferedBufferedReader.readLine()
            }
        } catch (e: IOException) {
            Timber.e("Error parsing raw resource file $fileName")
            e.printStackTrace()
        }
        return content.toString()
    }

    /**
     * Generate creation dates relative to the current time so that the
     * results from ordering by 'Hot' can be predetermined.
     *
     * In this way, the first 6 entries will be created within
     * the last 24 hours.
     *
     * 14400000 = millisInADay / 6
     */
    private fun creationDate(i: Int) = calendar.timeInMillis - i * 14400000
}