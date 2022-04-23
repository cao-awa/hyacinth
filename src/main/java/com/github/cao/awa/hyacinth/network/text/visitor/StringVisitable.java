package com.github.cao.awa.hyacinth.network.text.visitor;

import com.github.cao.awa.hyacinth.network.text.style.Style;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Unit;

import java.util.List;
import java.util.Optional;

/**
 * An object that can supply strings to a visitor,
 * with or without a style context.
 */
public interface StringVisitable {
    /**
     * Convenience object indicating the termination of a string visit.
     */
    Optional<Unit> TERMINATE_VISIT = Optional.of(Unit.INSTANCE);
    /**
     * An empty visitable that does not call the visitors.
     */
    StringVisitable EMPTY = new StringVisitable(){

        @Override
        public <T> Optional<T> visit(Visitor<T> visitor) {
            return Optional.empty();
        }

        @Override
        public <T> Optional<T> visit(StyledVisitor<T> styledVisitor, Style style) {
            return Optional.empty();
        }
    };

    /**
     * Supplies this visitable's literal content to the visitor.
     *
     * @return {@code Optional.empty()} if the visit finished, or a terminating
     * result from the {@code visitor}
     *
     * @param visitor the visitor
     */
    <T> Optional<T> visit(Visitor<T> visitor);

    /**
     * Supplies this visitable's literal content and contextual style to
     * the visitor.
     *
     * @return {@code Optional.empty()} if the visit finished, or a terminating
     * result from the {@code visitor}
     *
     * @param style the contextual style
     * @param styledVisitor the visitor
     */
    <T> Optional<T> visit(StyledVisitor<T> style, Style styledVisitor);

    /**
     * Creates a visitable from a plain string.
     *
     * @param string the plain string
     */
    static StringVisitable plain(final String string) {
        return new StringVisitable(){

            @Override
            public <T> Optional<T> visit(Visitor<T> visitor) {
                return visitor.accept(string);
            }

            @Override
            public <T> Optional<T> visit(StyledVisitor<T> styledVisitor, Style style) {
                return styledVisitor.accept(style, string);
            }
        };
    }

    /**
     * Creates a visitable from a plain string and a root style.
     *
     * @param style the root style
     * @param string the plain string
     */
    static StringVisitable styled(final String string, final Style style) {
        return new StringVisitable(){

            @Override
            public <T> Optional<T> visit(Visitor<T> visitor) {
                return visitor.accept(string);
            }

            @Override
            public <T> Optional<T> visit(StyledVisitor<T> styledVisitor, Style style2) {
                return styledVisitor.accept(style.withParent(style2), string);
            }
        };
    }

    /**
     * Concats multiple string visitables by the order they appear in the array.
     *
     * @param visitables an array or varargs of visitables
     */
    static StringVisitable concat(StringVisitable... visitables) {
        return StringVisitable.concat(ImmutableList.copyOf(visitables));
    }

    /**
     * Concats multiple string visitables by the order they appear in the list.
     *
     * @param visitables a list of visitables
     */
    static StringVisitable concat(final List<? extends StringVisitable> visitables) {
        return new StringVisitable(){

            @Override
            public <T> Optional<T> visit(Visitor<T> visitor) {
                for (StringVisitable stringVisitable : visitables) {
                    Optional<T> optional = stringVisitable.visit(visitor);
                    if (!optional.isPresent()) continue;
                    return optional;
                }
                return Optional.empty();
            }

            @Override
            public <T> Optional<T> visit(StyledVisitor<T> styledVisitor, Style style) {
                for (StringVisitable stringVisitable : visitables) {
                    Optional<T> optional = stringVisitable.visit(styledVisitor, style);
                    if (!optional.isPresent()) continue;
                    return optional;
                }
                return Optional.empty();
            }
        };
    }

    default String getString() {
        StringBuilder stringBuilder = new StringBuilder();
        this.visit(string -> {
            stringBuilder.append(string);
            return Optional.empty();
        });
        return stringBuilder.toString();
    }

    interface Visitor<T> {
        Optional<T> accept(String var1);
    }

    interface StyledVisitor<T> {
        Optional<T> accept(Style var1, String var2);
    }
}

