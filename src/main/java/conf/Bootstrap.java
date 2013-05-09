/**
 * Copyright (C) 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package conf;

import java.io.IOException;

import javax.inject.Singleton;

import models.Post;
import models.User;
import ninja.lifecycle.Dispose;
import ninja.lifecycle.Start;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.avaje.ebean.Ebean;

@Singleton
public class Bootstrap {

	private static final Logger log = LoggerFactory.getLogger(Bootstrap.class);

	@Start(order = 90)
	public void startService() throws IOException {
		User user1 = new User("tarou@test.com", "secret", "Tarou");
		User user2 = new User("suzuki@test.com", "secret", "Suzuki");
		
		Ebean.save(user1);
		Ebean.save(user2);
		
		Post post1 = new Post(user1, "Scala Programming Language", 
				"Scala is a multi-paradigm programming language designed to\n"
				+ "integrate features of object-oriented programming and functional programming.\n\n"
				+ "The name Scala is a portmanteau of \"scalable\" and \"language\",\n"
				+ "signifying that it is designed to grow with the demands of its users.");
		
		Post post2 = new Post(user1, "test data of YABE", "test data !!");
		Post post3 = new Post(user2, "Ninja Framework!!!", "Cool!!!!!");
		
		Ebean.save(post1);
		Ebean.save(post2);
		Ebean.save(post3);

		post1.addComment("Guest", "I knew that ...");
		post1.addComment("Igamon", "ninnin");
		post3.addComment("Hanzo", "You are right!");
	}

	@Dispose(order = 90)
	public void stopService() {
	}

}
