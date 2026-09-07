package com.example.Entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "campaigns")
public class Campaign {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 80)
    private String ownerId;
    @Column(nullable = false, length = 160)
    private String organizer;
    @Column(length = 30)
    private String phone;
    @Column(nullable = false, length = 180)
    private String title;
    @Column(nullable = false, length = 80)
    private String category;
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal goal;
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal raised = BigDecimal.ZERO;
    @Column(nullable = false, length = 120)
    private String city;
    @Lob
    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String story;
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String image;
    @Column(length = 255)
    private String imageContentType;
    @Lob
    @Column(name = "image_data", columnDefinition = "LONGBLOB")
    private byte[] imageData;
    @Column(nullable = false)
    private Integer donorCount = 0;
    @Column(nullable = false, length = 30)
    private String status = "Active";
    @Column(nullable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist void prePersist(){ var n=LocalDateTime.now(); if(createdAt==null)createdAt=n; updatedAt=n; if(raised==null)raised=BigDecimal.ZERO; if(donorCount==null)donorCount=0; }
    @PreUpdate void preUpdate(){ updatedAt=LocalDateTime.now(); }

    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public String getOwnerId(){return ownerId;} public void setOwnerId(String ownerId){this.ownerId=ownerId;}
    public String getOrganizer(){return organizer;} public void setOrganizer(String organizer){this.organizer=organizer;}
    public String getPhone(){return phone;} public void setPhone(String phone){this.phone=phone;}
    public String getTitle(){return title;} public void setTitle(String title){this.title=title;}
    public String getCategory(){return category;} public void setCategory(String category){this.category=category;}
    public BigDecimal getGoal(){return goal;} public void setGoal(BigDecimal goal){this.goal=goal;}
    public BigDecimal getRaised(){return raised;} public void setRaised(BigDecimal raised){this.raised=raised;}
    public String getCity(){return city;} public void setCity(String city){this.city=city;}
    public String getStory(){return story;} public void setStory(String story){this.story=story;}
    public String getImage(){return image;} public void setImage(String image){this.image=image;}
    public Integer getDonorCount(){return donorCount;} public void setDonorCount(Integer donorCount){this.donorCount=donorCount;}
    public String getStatus(){return status;} public void setStatus(String status){this.status=status;}
    public LocalDateTime getCreatedAt(){return createdAt;} public LocalDateTime getUpdatedAt(){return updatedAt;}
	@com.fasterxml.jackson.annotation.JsonIgnore
 public byte[] getImageData() {
		return imageData;
	}
	public void setImageData(byte[] imageData) {
		this.imageData = imageData;
	}
	public String getImageContentType() {
		return imageContentType;
	}
	public void setImageContentType(String imageContentType) {
		this.imageContentType = imageContentType;
	}
    
}
