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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Post;
import ninja.Context;
import ninja.Result;
import ninja.Results;
import ninja.i18n.Lang;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ApplicationController {

	@Inject
	public Logger logger;

	@Inject
	Lang lang;

	public Result index(Context context) {

		List<Post> posts = Post.findRecent(11);

		Post frontPost = posts.isEmpty() ? null : posts.remove(0);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("frontPost", frontPost);
		map.put("olderPosts", posts);

		return Results.html().render(map);

	}

}
