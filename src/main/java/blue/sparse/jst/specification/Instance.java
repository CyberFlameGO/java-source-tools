package blue.sparse.jst.specification;

public abstract class Instance<T extends Instance<T>> {
	private Specification<T> specification;

	public Instance(Specification<T> specification) {

		this.specification = specification;
	}

	public Specification<T> getSpecification() {
		return specification;
	}
}
