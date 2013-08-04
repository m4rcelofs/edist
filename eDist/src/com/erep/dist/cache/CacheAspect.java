package com.erep.dist.cache;

import java.util.logging.Logger;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Aspect for handling cacheable methods.
 * 
 * @author http://urmincek.wordpress.com/2009/09/06/simple-caching-with-aspectj/
 * @since 31/10/2010
 * @since 19/09/2012
 */
// TODO use lower level API for expiration
// https://developers.google.com/appengine/docs/java/javadoc/com/google/appengine/api/memcache/package-summary
@Aspect
public class CacheAspect {

	private static final Logger	logger	= Logger.getLogger(CacheAspect.class
												.getName());

	/**
	 * Pointcut for all methods annotated with <code>@Cacheable</code>
	 */
	@Pointcut("execution(@Cacheable * *.*(..))")
	private void cache() {}

	@Around("cache()")
	public Object aroundCachedMethods(final ProceedingJoinPoint thisJoinPoint)
			throws Throwable {

		if (ServerConstants.CACHE_ENABLED) {
			logger.fine("Execution of Cacheable method catched");

			String key = CacheFacade.createKey(thisJoinPoint);
			logger.info("Key = " + key);

			Object result;
			if (!CacheFacade.containsKey(key)) {
				logger.fine("Result not yet cached. Must be calculated...");
				result = thisJoinPoint.proceed();
				logger.fine("Storing calculated value '" + result
						+ "' to cache");
				CacheFacade.put(key, result);
			} else {
				result = CacheFacade.get(key);
				logger.fine("Result '" + result + "' was found in cache");
			}
			return result;
		} else
			return thisJoinPoint.proceed();
	}

}
