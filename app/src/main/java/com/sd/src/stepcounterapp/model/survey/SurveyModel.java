package com.sd.src.stepcounterapp.model.survey;

import java.util.ArrayList;
import java.util.List;

public class SurveyModel {
	
	/**
	 * status : 200
	 * message : Success
	 * data : [{"_id":"5d35904708cc29292ea6936b","name":"travel preferences","expireOn":"2020-11-21T04:32:18.758Z","earningToken":10,"products":[{"_id":"5d35a8c4ea9c4b2fc26aa2d1","quesType":"checkbox","question":"test123456789","option1":"opt1","option2":"opt2","option3":"opt3","option4":"opt4","answer":["opt1","opt3"]},{"_id":"5d35a8c4ea9c4b2fc26aa2d2","quesType":"radio","question":"test123","option1":"opt1","option2":"opt2","option3":"opt3","option4":"opt4","answer":["opt1"]}]},{"_id":"5d35905708cc29292ea6936c","name":"app feedback","expireOn":"2020-11-21T04:32:18.758Z","earningToken":10,"products":[{"_id":"5d35a8c1ea9c4b2fc26aa2d0","quesType":"checkbox","question":"test1234567890","option1":"opt1","option2":"opt2","option3":"opt3","option4":"opt4","answer":["opt1","opt3"]},{"_id":"5d35a8c8ea9c4b2fc26aa2d2","quesType":"checkbox","question":"test12345678","option1":"opt1","option2":"opt2","option3":"opt3","option4":"opt4","answer":["opt1","opt3"]}]}]
	 */
	
	private int status;
	private String message;
	private ArrayList<DataBean> data;
	
	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public ArrayList<DataBean> getData() {
		return data;
	}
	
	public void setData(ArrayList<DataBean> data) {
		this.data = data;
	}
	
	public static class DataBean {
		/**
		 * _id : 5d35904708cc29292ea6936b
		 * name : travel preferences
		 * expireOn : 2020-11-21T04:32:18.758Z
		 * earningToken : 10
		 * products : [{"_id":"5d35a8c4ea9c4b2fc26aa2d1","quesType":"checkbox","question":"test123456789","option1":"opt1","option2":"opt2","option3":"opt3","option4":"opt4","answer":["opt1","opt3"]},{"_id":"5d35a8c4ea9c4b2fc26aa2d2","quesType":"radio","question":"test123","option1":"opt1","option2":"opt2","option3":"opt3","option4":"opt4","answer":["opt1"]}]
		 */
		
		private String _id;
		private String name;
		private String expireOn;
		private int earningToken;
		private List<ProductsBean> products;
		
		public String get_id() {
			return _id;
		}
		
		public void set_id(String _id) {
			this._id = _id;
		}
		
		public String getName() {
			return name;
		}
		
		public void setName(String name) {
			this.name = name;
		}
		
		public String getExpireOn() {
			return expireOn;
		}
		
		public void setExpireOn(String expireOn) {
			this.expireOn = expireOn;
		}
		
		public int getEarningToken() {
			return earningToken;
		}
		
		public void setEarningToken(int earningToken) {
			this.earningToken = earningToken;
		}
		
		public List<ProductsBean> getProducts() {
			return products;
		}
		
		public void setProducts(List<ProductsBean> products) {
			this.products = products;
		}
		
		public static class ProductsBean {
			/**
			 * _id : 5d35a8c4ea9c4b2fc26aa2d1
			 * quesType : checkbox
			 * question : test123456789
			 * option1 : opt1
			 * option2 : opt2
			 * option3 : opt3
			 * option4 : opt4
			 * answer : ["opt1","opt3"]
			 */
			
			private String _id;
			private String quesType;
			private String question;
			private String option1;
			private String option2;
			private String option3;
			private String option4;
			private List<String> answer;
			
			public String get_id() {
				return _id;
			}
			
			public void set_id(String _id) {
				this._id = _id;
			}
			
			public String getQuesType() {
				return quesType;
			}
			
			public void setQuesType(String quesType) {
				this.quesType = quesType;
			}
			
			public String getQuestion() {
				return question;
			}
			
			public void setQuestion(String question) {
				this.question = question;
			}
			
			public String getOption1() {
				return option1;
			}
			
			public void setOption1(String option1) {
				this.option1 = option1;
			}
			
			public String getOption2() {
				return option2;
			}
			
			public void setOption2(String option2) {
				this.option2 = option2;
			}
			
			public String getOption3() {
				return option3;
			}
			
			public void setOption3(String option3) {
				this.option3 = option3;
			}
			
			public String getOption4() {
				return option4;
			}
			
			public void setOption4(String option4) {
				this.option4 = option4;
			}
			
			public List<String> getAnswer() {
				return answer;
			}
			
			public void setAnswer(List<String> answer) {
				this.answer = answer;
			}
		}
	}
}
