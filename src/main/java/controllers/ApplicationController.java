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

package controllers;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import models.Post;
import ninja.Context;
import ninja.Result;
import ninja.Results;
import ninja.i18n.Lang;
import ninja.params.Param;
import ninja.params.PathParam;
import ninja.validation.Length;
import ninja.validation.Required;
import ninja.validation.Validation;

import org.slf4j.Logger;

import com.google.common.collect.Maps;
import com.google.inject.Inject;

//@Singleton TODO HttpServletRequestのInjectができなくなるため
public class ApplicationController {

	@Inject
	public Logger logger;

	@Inject
	Lang lang;

	@Inject
	HttpServletRequest request;

	public Result index(Context context) {

		List<Post> posts = Post.findRecent(11);

		Post frontPost = posts.isEmpty() ? null : posts.remove(0);

		Map<String, Object> map = newHashMap();
		map.put("frontPost", frontPost);
		map.put("olderPosts", posts);

		return Results.html().render(map);

	}

	public Result show(@PathParam("id") Long id, Context context) {
		return Results.html().render(getPostMap(id));
	}
	
	public Result postComment(Validation validation,
			@PathParam("postId") Long postId,
			@Param("author") @Required @Length(min = 1) String author,
			@Param("content") @Required @Length(min = 1) String content,
			Context context) {
		
		Map<String, Object> map = getPostMap(postId);
		
		if (validation.hasViolations()) {
			map.put("ifErrors", true);
			map.put("author", author);
			map.put("content", content);
		} else {
			Post post = Post.findById(postId);
			post.addComment(author, content);
			
			context.getFlashCookie().success("Thanks for posting %s", author);
			map.putAll(context.getFlashCookie().getCurrentFlashCookieData());
		}

		return Results.html().template("/views/ApplicationController/show.ftl.html").render(map);
	}
	
	private Map<String, Object> getPostMap(Long id) {
		Map<String, Object> map = newHashMap();
		Post post = Post.findById(id);
		map.put("post", post);

		map.put("previous", post.previous());
		map.put("next", post.next());
		
		return map;
	}

	private Map<String, Object> newHashMap() {
		Map<String, Object> map = Maps.newHashMap();
		map.put("contextPath", "".equals(request.getContextPath()) ? "/" : request.getContextPath());
		return map;
	}

}
