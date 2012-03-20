package newseman;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.util.AttributeImpl;

public class SemanticTagAttributeImpl extends AttributeImpl implements
		SemanticTagAttribute, Cloneable, Serializable {

	private ArrayList<String> semanticTags = new ArrayList<String>();

	public void setSemanticTags(List<String> tags) {
		semanticTags.clear();
		semanticTags.addAll(tags);
	}

	public List<String> getSemanticTags() {
		return semanticTags;
	}

	public boolean hasSemanticTags() {
		if (semanticTags.size() > 0)
			return true;
		return false;
	}

	public void clear() {
		this.semanticTags.clear();
	}

	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}

		if (other instanceof SemanticTagAttributeImpl) {
			return semanticTags == ((SemanticTagAttributeImpl) other).semanticTags;
		}

		return false;
	}

	public int hashCode() {
		return semanticTags.hashCode();
	}

	public void copyTo(AttributeImpl target) {
		SemanticTagAttributeImpl t = (SemanticTagAttributeImpl) target;
		t.setSemanticTags(this.getSemanticTags());
	}

}
