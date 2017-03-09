package net.cassite.pure.aop;

import net.cassite.style.interfaces.RFunc0;

/**
 * suggest that the weaver is aware of it's target
 *
 * @param <Target> target type
 * @author lemon
 * @since 0.2.1
 */
public interface TargetAware<Target> {
    /**
     * set proxy target to the weaver<br>
     * The implementation should record the argument as a field
     *
     * @param targetFunc the function to retrieve proxy target
     */
    void targetAware(final RFunc0<Target> targetFunc);
}
