
CREATE TABLE IF NOT EXISTS BusLine (
  line varchar(20) not null,
  description varchar(255),
  primary key (line)
);

CREATE TABLE IF NOT EXISTS BusStop (
  id varchar(20) not null,
  name varchar(255) not null,
  lat double precision,
  lng double precision,
  primary key (id)
);

CREATE TABLE IF NOT EXISTS BusLineStop (
  stopId varchar(20) not null,
  lineId varchar(20) not null,
  sequenceNumber smallint not null,
  primary key(stopId, lineId, sequenceNumber),
  foreign key (stopId) references BusStop(id),
  foreign key (lineId) references BusLine(line)
);


CREATE TABLE IF NOT EXISTS users(
   username varchar(255) NOT NULL,
   password varchar(20) NOT NULL,
   enabled boolean NOT NULL DEFAULT FALSE,
   primary key(username)
);

CREATE TABLE IF NOT EXISTS user_roles (
  user_role_id SERIAL PRIMARY KEY,
  username varchar(255) NOT NULL,
  role varchar(20) NOT NULL,
  UNIQUE (username,role),
  FOREIGN KEY (username) REFERENCES users (username)
);



CREATE TABLE IF NOT EXISTS message(
    id serial,
    username varchar(255),
    message varchar(65535),
    data timestamp without time zone,
    topic varchar(255),
    primary key(id)
);


CREATE TABLE IF NOT EXISTS activationCode (
  username varchar(255) NOT NULL,
  code varchar(255) NOT NULL,
  PRIMARY KEY (username)
);

CREATE TABLE IF NOT EXISTS rateUser (
  nickname varchar(255) NOT NULL,
  idsegnalation integer NOT NULL,
  rate int NOT NULL,
  primary key(nickname, idsegnalation),
  foreign key (nickname) references dettaglioUser(nickname),
  foreign key (idsegnalation) references segnalazioni(id)
);
  
CREATE TABLE IF NOT EXISTS dettaglioUser(
    nickname varchar(255) unique,
    email varchar(255) primary key,
    gender int,
    eta int,
    istruzione int,
    occupazione int, 
    hasCar boolean,
    annoimmatricolazione int,
    carburante int,
    useCarSharing boolean,
    fornitoreSharing int,
    useBike boolean,
    useBikeSharing boolean,
    useMezziPubblici boolean,
    tipoViaggio int,
    foto varchar(65535)
);

CREATE TABLE IF NOT EXISTS segnalazioni (
  id serial primary key,
  nickname varchar(255) NOT NULL,
  lat double precision,
  lng double precision,
  tipo int,
  rate double precision,
  count int,
  dataInizio timestamp without time zone NOT NULL,
  dataFine timestamp without time zone,
  indirizzo varchar(255),
  FOREIGN KEY (nickname) REFERENCES dettaglioUser (nickname)
);

CREATE FUNCTION vota() RETURNS trigger AS $emp_stamp$
DECLARE
newcount integer;
flag integer;
newrate double precision;
somma double precision;
BEGIN
	
new.dataFine = localtimestamp + interval '5 minutes';
if(new.count = 0) then
	SELECT count, rate into newcount, newrate
	from segnalazioni
	where id = new.id;

somma = newcount*newrate;
newcount = newcount +1;
somma = somma + new.rate;

elseif (new.count = 1) then
	SELECT count, rate into newcount, newrate
	from segnalazioni
	where id = new.id;

somma = newcount*newrate;
somma = somma + new.rate;
	
end if;

new.rate = somma/newcount;
new.count = newcount;

return new;
END;
$emp_stamp$ LANGUAGE plpgsql;

CREATE TRIGGER updateRate
BEFORE UPDATE on segnalazioni 
FOR EACH ROW
EXECUTE PROCEDURE vota();

INSERT INTO users(username,password,enabled) VALUES ('jack','jack', true);
INSERT INTO users(username,password,enabled) VALUES ('peter','peter', true);

INSERT INTO user_roles (username, role) VALUES ('jack', 'ROLE_USER');
INSERT INTO user_roles (username, role) VALUES ('jack', 'ROLE_ADMIN');
INSERT INTO user_roles (username, role) VALUES ('peter', 'ROLE_USER');

