package com.comag10.crowdflower.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.comag10.crowdflower.model.Users;


@Repository
public class UserDaoImpl implements UserDao {

	private static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sf){
		this.sessionFactory = sf;
	}

	public void addUser(Users u) {
		// TODO Auto-generated method stub
		Session session = this.sessionFactory.getCurrentSession();

		try {
			
			session.persist(u);
			//session.save(u);
		} catch( Exception e ) {
			System.out.println(e.getMessage());
		}
	}

	public void updateUser(Users u) {
		// TODO Auto-generated method stub
		Session session = this.sessionFactory.getCurrentSession();
		session.update(u);
		logger.info("Person updated successfully, Person Details="+u);
	}

	@SuppressWarnings("unchecked")
	public List<Users> listUsers() {
		// TODO Auto-generated method stub
		Session session = this.sessionFactory.getCurrentSession();
		List<Users> usersList = session.createQuery(" from Users").list();
		for(Users p : usersList){
			logger.info("Person List::"+p);
		}
		return usersList;
	}

	public Users getUserById(int id) {
		// TODO Auto-generated method stub
		Session session = this.sessionFactory.getCurrentSession();      
		Users p = (Users) session.load(Users.class, new Integer(id));
		logger.info("Person loaded successfully, Person details="+p);
		return p;
	}

	public void removeUser(int id) {
		// TODO Auto-generated method stub
		Session session = this.sessionFactory.getCurrentSession();
		Users p = (Users) session.load(Users.class, new Integer(id));
		if(null != p){
			session.delete(p);
		}
		logger.info("Person deleted successfully, person details="+p);
	}

	@SuppressWarnings("unchecked")
	public Users authnticateUser(Users u) {
		// TODO Auto-generated method stub
		Session session = this.sessionFactory.getCurrentSession();
		List<Users> usersList = session.createQuery(" FROM Users WHERE name='"+u.getUsername()+"' AND password='"+u.getPassword()+"'").list();
		
		return usersList.get(0);
	}
	
	
	@SuppressWarnings("unchecked")
	public Boolean isAlreadyExistingUser(String username) {
		// TODO Auto-generated method stub
		Session session = this.sessionFactory.getCurrentSession();
		List<Users> usersList = session.createQuery(" FROM Users WHERE username='"+username+"'").list();
		
		if( usersList.size()<=0 )
			return false;
		else 
			return true;
	}
	
}