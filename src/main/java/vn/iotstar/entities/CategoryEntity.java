package vn.iotstar.entities;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "categories")
@NamedQuery(name="CategoryEntity.findAll", query="SELECT c FROM CategoryEntity c")
public class CategoryEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cate_id")
	private int cateid;
	
	@Column(name="cate_name", columnDefinition = "NVARCHAR(255)")
	private String catename;
	
	@Column(name = "icons", columnDefinition = "NVARCHAR(MAX)")
	private String icon;

	@Column(name = "user_id", columnDefinition = "INT")
	private int userid;
	

}
