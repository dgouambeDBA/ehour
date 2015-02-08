package net.rrm.ehour.backup.service.restore;

import net.rrm.ehour.backup.domain.ParseSession;
import net.rrm.ehour.backup.domain.ParserUtil;
import net.rrm.ehour.backup.service.backup.BackupEntity;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserRole;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.Serializable;

/**
 * User: thies
 * Date: 11/29/10
 * Time: 11:28 PM
 */
public class UserRoleParser {
    private UserRoleParserDao dao;
    private PrimaryKeyCache keyCache;
    private final BackupEntity userRoleBackupEntity;

    public UserRoleParser(UserRoleParserDao dao, PrimaryKeyCache keyCache, BackupEntity userRoleBackupEntity) {
        this.dao = dao;
        this.keyCache = keyCache;
        this.userRoleBackupEntity = userRoleBackupEntity;
    }

    public void parseUserRoles(XMLEventReader reader, ParseSession status) throws XMLStreamException {
        while (reader.nextTag().isStartElement()) {
            parseUserRole(reader, status);
        }
    }

    private void parseUserRole(XMLEventReader reader, ParseSession status) throws XMLStreamException {
        XMLEvent event;
        String role = null;
        String userId = null;

        while ((event = reader.nextTag()).isStartElement()) {
            StartElement element = event.asStartElement();
            String name = element.getName().getLocalPart();
            String data = ParserUtil.parseNextEventAsCharacters(reader);

            if (name.equalsIgnoreCase("ROLE")) {
                role = data;
            } else if (name.equalsIgnoreCase("USER_ID")) {
                userId = data;
            }
        }

        if (userId != null && role != null) {
            Serializable newUserId = keyCache.getKey(User.class, Integer.parseInt(userId));

            // validate = string, actual import = integer
            Integer castedUserId = newUserId instanceof String ? Integer.parseInt((String) newUserId) : (Integer) newUserId;

            User user = dao.findUser(castedUserId);
            UserRole userRole = dao.findUserRole(role);

            user.addUserRole(userRole);
            dao.persistUser(user);

            status.addInsertion(userRoleBackupEntity);
        } else {
            status.addError(userRoleBackupEntity, "No userID (" + userId + ") or role (" + role + ") found");
        }
    }
}
