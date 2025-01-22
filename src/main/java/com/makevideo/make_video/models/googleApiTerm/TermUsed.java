package com.makevideo.make_video.models.googleApiTerm;

import java.util.Objects;

public class TermUsed {
    private String term;
    private Integer manyTimes;

    public TermUsed(String term, Integer manyTimes) {
        this.term = term;
        this.manyTimes = manyTimes;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        TermUsed termUsed = (TermUsed) object;
        return Objects.equals(term, termUsed.term);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(term);
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public Integer getManyTimes() {
        return manyTimes;
    }

    public void setManyTimes(Integer manyTimes) {
        this.manyTimes = manyTimes;
    }
}
