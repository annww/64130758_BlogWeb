package ntu.hongdta_64130758.services.interf;

import java.util.Optional;

import ntu.hongdta_64130758.models.User;

public interface IUserService{
	User findByUsername(String username);
}
