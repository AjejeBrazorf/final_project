package com.javasampleapproach.security.model;

import java.io.Serializable;

public class RoleId implements Serializable {
	 
		private String email;
		private String role;
	 
		public RoleId() {
	 
		}
		
		public RoleId(String email, String role) {
			this.email = email;
			this.role = role;
		}

		public String getEmail() {
			return email;
		}

		public String getRole() {
			return role;
		}
	 
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((email == null) ? 0 : email.hashCode());
			result = prime * result + role.hashCode();
			return result;
		}
	 
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			RoleId other = (RoleId) obj;
			if (email == null) {
				if (other.email != null)
					return false;
			} else if (!email.equals(other.email))
				return false;
			if (role.hashCode() != other.hashCode())
				return false;
			return true;
		}

}
