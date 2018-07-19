package pl.coddlers.git.models.event;

public class ProjectDTO {
	private Long id;
	private String name;
	private String description;
	private String web_url;
	private String avatar_url;
	private String git_ssh_url;
	private String git_http_url;
	private String namespace;
	private Integer visibility_level;
	private String path_with_namespace;
	private String default_branch;
	private String homepage;
	private String url;
	private String ssh_url;
	private String http_url;

	public ProjectDTO(Long id, String name, String description, String web_url, String avatar_url, String git_ssh_url, String git_http_url, String namespace, Integer visibility_level, String path_with_namespace, String default_branch, String homepage, String url, String ssh_url, String http_url) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.web_url = web_url;
		this.avatar_url = avatar_url;
		this.git_ssh_url = git_ssh_url;
		this.git_http_url = git_http_url;
		this.namespace = namespace;
		this.visibility_level = visibility_level;
		this.path_with_namespace = path_with_namespace;
		this.default_branch = default_branch;
		this.homepage = homepage;
		this.url = url;
		this.ssh_url = ssh_url;
		this.http_url = http_url;
	}

	public ProjectDTO() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getWeb_url() {
		return web_url;
	}

	public void setWeb_url(String web_url) {
		this.web_url = web_url;
	}

	public String getAvatar_url() {
		return avatar_url;
	}

	public void setAvatar_url(String avatar_url) {
		this.avatar_url = avatar_url;
	}

	public String getGit_ssh_url() {
		return git_ssh_url;
	}

	public void setGit_ssh_url(String git_ssh_url) {
		this.git_ssh_url = git_ssh_url;
	}

	public String getGit_http_url() {
		return git_http_url;
	}

	public void setGit_http_url(String git_http_url) {
		this.git_http_url = git_http_url;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public Integer getVisibility_level() {
		return visibility_level;
	}

	public void setVisibility_level(Integer visibility_level) {
		this.visibility_level = visibility_level;
	}

	public String getPath_with_namespace() {
		return path_with_namespace;
	}

	public void setPath_with_namespace(String path_with_namespace) {
		this.path_with_namespace = path_with_namespace;
	}

	public String getDefault_branch() {
		return default_branch;
	}

	public void setDefault_branch(String default_branch) {
		this.default_branch = default_branch;
	}

	public String getHomepage() {
		return homepage;
	}

	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSsh_url() {
		return ssh_url;
	}

	public void setSsh_url(String ssh_url) {
		this.ssh_url = ssh_url;
	}

	public String getHttp_url() {
		return http_url;
	}

	public void setHttp_url(String http_url) {
		this.http_url = http_url;
	}

	@Override
	public String toString() {
		return "ProjectDTO{" +
				"id=" + id +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", web_url='" + web_url + '\'' +
				", avatar_url='" + avatar_url + '\'' +
				", git_ssh_url='" + git_ssh_url + '\'' +
				", git_http_url='" + git_http_url + '\'' +
				", namespace='" + namespace + '\'' +
				", visibility_level='" + visibility_level + '\'' +
				", path_with_namespace='" + path_with_namespace + '\'' +
				", default_branch='" + default_branch + '\'' +
				", homepage='" + homepage + '\'' +
				", url='" + url + '\'' +
				", ssh_url='" + ssh_url + '\'' +
				", http_url='" + http_url + '\'' +
				'}';
	}
}
