package models;

import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.annotation.CreatedTimestamp;

@Entity
public class Post {

	private static final Charset ENCODING = Charset.forName("UTF-8");

	@Id
	public Long id;

	public final String title;

	@CreatedTimestamp
	public Timestamp postedAt;

	@Lob()
	private final byte[] content;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	public final User author;

	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
	public List<Comment> comments;

	public Post(User author, String title, String content) {
		this.comments = new ArrayList<Comment>();
		this.author = author;
		this.title = title;
		this.content = content.getBytes(ENCODING);
	}

	public Post addComment(String author, String content) {
		Comment newComment = new Comment(this, author, content);
		Ebean.save(newComment);
		this.comments.add(newComment);
		Ebean.save(this);
		return this;
	}
	
	public Post previous() {
		return Ebean.find(Post.class)
				.fetch("author")
				.where()
				.lt("postedAt", this.postedAt)
				.orderBy("postedAt desc")
				.setMaxRows(1)
				.findUnique();
	}
	
	public Post next() {
		return Ebean.find(Post.class)
				.fetch("author")
				.where()
				.gt("postedAt", this.postedAt)
				.orderBy("postedAt asc")
				.setMaxRows(1)
				.findUnique();
	}

	public static List<Post> findRecent(int maxrows) {
		return Ebean.find(Post.class)
				.fetch("author")
				.orderBy("postedAt desc")
				.setMaxRows(maxrows)
				.findList();

	}
	
	public static Post findById(long id) {
		return Ebean.find(Post.class)
				.fetch("author")
				.where()
				.eq("id", id)
				.findUnique();
	}

	public String getContent() {
		return new String(this.content, ENCODING);
	}
}
