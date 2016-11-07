package net.adamjak.thomas.graph.interfaces.anot;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Tomas Adamjak on 17.7.2016.
 * Copyright 2016, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
/**
 * Class annotation whitch mark class ready for benchmark.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Benchmarked
{
}
